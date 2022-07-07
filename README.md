# jiraCloneSpringBoot

Backend for a small React application I did in order to get up to speed using React.

In order to run it you have to set up a postgreSQL database called ``jira-clone`` and add it to the ``application.properties`` file.
Make sure ``spring.jpa.hibernate.ddl-auto=create`` is set the first time you run the app.

If you have maven installed on and added to your PATH you can run ``maven spring-boot:run``,
otherwise use ``./mvnw spring-boot:run``

API path for testing with Postman is ``api/issue`` and the whole things runs locally on ``localhost:8080``