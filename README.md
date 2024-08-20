## to run the application:
1. clone the git
2. ```bash
   docker compose build
   ```
3. ```bash
   docker compe up -d
   ```
4. the api can be accessed at localhost:8080/swager-ui.html

the assignment:
# WEBSTEP Back-End Test
Create a simple REST service for a task board that allows a user to create and manage different
lists of tasks which are persisted between devices. The application should implement the following
user stories:

1. As a user, I can view all lists and tasks which have been created
2. As a user, I can create an empty list with a name property
3. As a user, I can add new tasks to an existing list with a name and description property.
4. As a user, I can update the name and description of a tasks within a list
5. As a user, I can delete a task from a list
6. As a user, I can delete an entire list with all its tasks
7. As a user, I can move tasks to a different list

The user stories are in priority order, please complete as many as you but don’t spend more than 3
hours. This test will be used for further discussion during the technical interview, so it’s not graded
based on completion.

The application can be designed and built in any language utilizing frameworks or tools which you are
comfortable with.

We will be reviewing how you structure new applications, which processes, practices and standards
you put in place. Some of the things we may look for are:
● Test coverage and types of tests
● Architectural design choices
● How to build and run the application (Docker? Directly on host? Cloud provider?)
● Relational or non-relational database
● Developer onboarding experience

When you are finished with everything please push the code to a public git repository. Include a
README file with directions on how to install and start the application. Then send us the link. Thank
you and we do hope you will have fun doing this assignment!
