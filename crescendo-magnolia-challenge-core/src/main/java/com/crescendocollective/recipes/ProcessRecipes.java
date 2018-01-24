package com.crescendocollective.recipes;

import info.magnolia.commands.impl.BaseRepositoryCommand;
import info.magnolia.context.Context;
import info.magnolia.context.MgnlContext;
import info.magnolia.dam.api.AssetProviderRegistry;
import info.magnolia.dam.jcr.AssetNodeTypes;
import info.magnolia.dam.jcr.DamConstants;
import info.magnolia.dam.jcr.JcrAssetProvider;
import info.magnolia.dam.jcr.JcrFolder;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.objectfactory.Components;
import java.io.IOException;
import javax.jcr.RepositoryException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jackrabbit.commons.JcrUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.jcr.Node;
import javax.jcr.Session;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ThreadLocalRandom;

public class ProcessRecipes extends BaseRepositoryCommand {

    @Override
    public boolean execute(Context context) throws Exception {
        Session recipeSession = context.getJCRSession("website");
        Node rootNode = recipeSession.getRootNode();

        String recipesCaption = "recipes";
        Node recipes = rootNode.addNode(recipesCaption, "mgnl:page");
        NodeTypes.Renderable.set(recipes, "crescendo-magnolia-challenge:pages/recipes/recipes");
        PropertyUtil.setProperty(recipes, "title", "Recipes Title");
        recipeSession.save();

        AssetProviderRegistry assetProviderRegistry = Components.getComponent(AssetProviderRegistry.class);
        JcrAssetProvider jcrAssetProvider = (JcrAssetProvider) assetProviderRegistry.getProviderById(DamConstants.DEFAULT_JCR_PROVIDER_ID);
        JcrFolder assetFolder = (JcrFolder) jcrAssetProvider.getRootFolder();
        Node assetFolderNode = assetFolder.getNode();
        Session imageSession = MgnlContext.getJCRSession(DamConstants.WORKSPACE);

        JSONArray jsonArray = sendRequest(context);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            processRecipe(object, recipes, assetFolderNode, imageSession);
        }

        imageSession.save();
        recipeSession.save();
        return true;
    }

    private JSONArray sendRequest(Context context) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpRequest = new HttpGet((String) context.getAttribute("recipesLink", 3));
        httpRequest.addHeader("accept", "application/json");
        HttpResponse httpResponse = client.execute(httpRequest);

        String json = IOUtils.toString(httpResponse.getEntity().getContent());
        return new JSONArray(json);
    }

    private void processRecipe(JSONObject jsonObject, Node recipes, Node assetFolderNode, Session imageSession)
                throws RepositoryException, IOException {
        Node recipe = recipes.addNode(jsonObject.getString("title"), "mgnl:page");
        NodeTypes.Renderable.set(recipe, "crescendo-magnolia-challenge:pages/recipes/recipe");
        PropertyUtil.setProperty(recipe, "title", jsonObject.getString("title"));
        PropertyUtil.setProperty(recipe, "description", jsonObject.getString("description"));
        PropertyUtil.setProperty(recipe, "prepTime", jsonObject.getString("prepTime"));
        PropertyUtil.setProperty(recipe, "cookTime", jsonObject.getString("cookTime"));
        PropertyUtil.setProperty(recipe, "servingSize", jsonObject.getString("servingSize"));

        String imageUrl = "http:" + jsonObject.getString("largeImageUrl");
        String imageExtension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);
        String imageTitle = Integer.toString(ThreadLocalRandom.current().nextInt()) + imageUrl.substring(imageUrl.lastIndexOf("."));
        String mimeType = URLConnection.guessContentTypeFromName(imageTitle);

        URL url = new URL(imageUrl);
        long contentLength = url.openConnection().getContentLength();
        BufferedImage image = ImageIO.read(url);

        int height = image.getHeight();
        int width = image.getWidth();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, imageExtension, os);
        InputStream imageStream = new ByteArrayInputStream(os.toByteArray());

        Node assetNode = JcrUtils.getOrAddNode(assetFolderNode, imageTitle, AssetNodeTypes.Asset.NAME);
        assetNode.setProperty(AssetNodeTypes.Asset.ASSET_NAME, imageTitle);

        Node assetResourceNode = populateResourceNode(assetNode, imageSession, imageTitle, imageStream, contentLength,
            mimeType, height, width);
    }

    private Node populateResourceNode(Node assetNode, Session imageSession, String imageTitle,
        InputStream imageStream, long contentLength, String mimeType, int height, int width)
        throws RepositoryException {
        Node assetResourceNode = JcrUtils.getOrAddNode(assetNode, AssetNodeTypes.AssetResource.RESOURCE_NAME, AssetNodeTypes.AssetResource.NAME);
        assetResourceNode.setProperty(AssetNodeTypes.AssetResource.DATA, imageSession.getValueFactory().createBinary(imageStream));
        assetResourceNode.setProperty(AssetNodeTypes.AssetResource.FILENAME, imageTitle);
        assetResourceNode.setProperty(AssetNodeTypes.AssetResource.EXTENSION, imageTitle);
        assetResourceNode.setProperty(AssetNodeTypes.AssetResource.SIZE, Long.toString(contentLength));
        assetResourceNode.setProperty(AssetNodeTypes.AssetResource.MIMETYPE, mimeType);
        assetResourceNode.setProperty(AssetNodeTypes.AssetResource.WIDTH, Integer.toString(width));
        assetResourceNode.setProperty(AssetNodeTypes.AssetResource.HEIGHT, Integer.toString(height));
        return assetResourceNode;
    }
}