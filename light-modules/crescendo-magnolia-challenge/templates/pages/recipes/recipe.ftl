<!DOCTYPE html>
<html>
    <head>
        [@cms.init /]
    </head>
    <body>
        <h1>${content.title!}</h1>
        <p><strong>Description:</strong> ${content.description!}</p>
        <p><strong>Preparation Time: </strong> ${content.prepTime!}</p>
        <p><strong>Cook Time: </strong> ${content.cookTime!}</p>
        <p><strong>Serving Size: </strong> ${content.servingSize!}</p>
    </body>
</html>