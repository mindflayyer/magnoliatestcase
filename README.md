# Local Environment Setup #

The below instructions will aide in the setup of a fresh magnolia development environment on your local machine. This project provides you with a magnolia community maven based webapp project (crescendo-magnolia-challenge-webapp), a blossom module which is a magnolia + spring framework integration module (crescendo-magnolia-challenge-core), and a light module w/ all the necessary frontend templates/areas/components. Feel free to reach out to us if you have any specific questions or have any problems.

### Software Pre-requisites ###

Download and install the required software below.

* Java JDK 1.8
* Maven 3.2.3 or later
* Mercurial
* Tomcat 8.0
* IntelliJ IDEA Ultimate

### Project Setup Instructions ###

1. Download & install above software pre-requisites.
2. Fork project into your own bitbucket account. (Send us this repository link when you are finished)
3. Follow [Maven setup](https://wiki.magnolia-cms.com/display/DEV/Maven+setup) documentation. Follow community edition specific instructions only.
4. Clone project into your desired location.
5. From command line: run `mvn clean install` in the root of the project. If you never used maven before, this may take 5-10 minutes to download all dependencies and result in `BUILD SUCCESS`.
6. Switch to IntelliJ
7. Close all open projects, click `Import Project.`
8. Choose `Import project from external model` and select `Maven`.
9. Keep clicking `Next` until you select SDK. Make sure you add/select the Java 1.8 JDK. Click `Next.`
10. Leave project name defaults and click `Finish.`
11. Click little down arrow on upper right hand corner (Select Run/Debug Configuration). Click `Edit Configurations.`
12. Click `+`, Scroll down to `Tomcat Server`, Select `Local`.
13. Name it `Tomcat 8.0` (you can name it whatever you want).
14. Under `Server` tab, Configure the `Application server` to be your new Tomcat v8.0.
15. Under `Server` tab, Put `-Xmx1024m` into the `VM Options` input.
16. Under `Deployment` tab, click `+`, select `Artifact`, and choose the exploded war.
17. Under `Deployment` tab, change `Application context` to `/local`.
18. Click `Apply`.
19. Under `Server` tab, change `On 'Update' action` and `On frame deactivation` to `Update classes and resources`.
20. Click `Apply` and `OK`.
21. Copy `magnolia.properties.example` under `crescendo-magnolia-challenge-webapp/src/main/webapp/WEB-INF/config/local` and rename to `magnolia.properties`.
22. Modify `magnolia.home` property for your case. This is the absolute path where Magnolia will store all data for Derby which is a flat file database. *Windows users: I recommend configuring this as far up to the Root level of your hard drive (i.e: C:/repos/crescendo-magnolia-challenge) due to Windows file path limits. (This may be fixed in later versions of Magnolia). En-sure read/write access to this folder to avoid file permission issues.*
23. Modify `magnolia.resources.dir` property for your case. This is just an absolute path to the light-modules folder in your project.
24. Say a prayer
25. Start Tomcat
26. You should be able to go to [http://localhost:8080/local](http://localhost:8080/local).
27. Login as `superuser` with the password `superuser`.
28. Enjoy!

### Starting on a fresh repository ###

Sometimes starting fresh will resolve many startup problems and make your project feel clean. Since you configured your `magnolia.home` property this setup is very easy. You just simply delete the configured `magnolia.home` folder. Often times when odd issues show up, it's wise to backup you magnolia home and starting fresh.

*Note: This will delete your entire JCR so anything not bootstrapped (i.e: pages/assets/categories) will be deleted.*

# Challenge #1 :: Contact us form to e-mail #

A test of your form processing skills by serving up a form on the GET request, and do form handling in a POST. We just ask you to take the provided contact form and have this information be sent via e-mail.

### Tasks to accomplish ###

* On GET, show the provided contactForm template.
* On POST, send this information via e-mail through any means necessary and respond with a thank you message.
* *Extra credit:* Make the contact recipient configurable via a dialog in the contactForm component.
* *Extra credit:* Provide backend validation ensuring all text fields have a value and the e-mail address is formatted correctly.

### Helpers ###

* We already provided you the contact template and the contactForm component frontend. See page/component/dialog definitions provided at /light-modules/crescendo-magnolia-challenge.
* Magnolia provides the means to tie a modelClass to components so you can implement Java business logic. See the [documentation here.](https://documentation.magnolia-cms.com/display/DOCS/Model)
* You can also use the provided blossom module (spring framework integration) to implement a blossom based component. See the [documentation here.](https://documentation.magnolia-cms.com/display/DOCS/Blossom+module#Blossommodule-Components)

# Challenge #2 :: Auto-create recipe pages with given JSON endpoint #

Say you were tasked with the migration of content from a legacy system into the Magnolia CMS. The way the migration would work is to read from an endpoint that returns JSON data and import all data and create magnolia pages/areas/components. The overall goal of this challenge is for you to show how you would import this data into Magnolia and provide the ability for a content editor to edit its content.

### Tasks to accomplish ###

* Read data from `http://www.johnsonville.com/recipes.top-rated.json`, parse the JSON, and create new recipe pages in magnolia.
* Organize data as page properties, areas, and components as you see fit.
* *Extra credit:* Download the large recipe image and import into the Magnolia DAM.
* *Extra credit:* Use the recipes page template to list all the recipe pages. Show title and provide a link to the recipe page.

### Helpers ###

* Think of this as a one time import so do not worry about trigger this over and over again. You can trigger this in mulitple ways but creating a [groovy script that fires a command](https://documentation.magnolia-cms.com/display/DOCS/Commands#Commands-ExecutingacommandintheGroovyconsole) is one recommended option.
* See the [JCR API documentation](https://documentation.magnolia-cms.com/display/DOCS/JCR+API) for help on creating pages programmatically.
* We already provided you with a recipes landing and recipe detail page templates. Create the recipes page template and add the recipe pages inside the recipes page.
* The recipe detail dialog only provides a few fields, we expect you to fill in any additional ones as you see fit. See magnolia's documentation related to [form fields](https://documentation.magnolia-cms.com/display/DOCS/List+of+fields).