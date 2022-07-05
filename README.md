# Description

Backend for a Survey Tool which allows surveys to be conducted.

# Endpoints

| **path**                            | **method**     | **description**                                           |
|-------------------------------------|----------------|-----------------------------------------------------------|
| /api/campaigns                      | GET            | Find all public campaigns                                 |
| /api/campaigns/{id}                 | PUT/GET/DELETE | Update, find or delete campaign                           |
| /api/campaigns/{id}/survey          | GET            | Find survey by campaign ID                                |
| /api/campaigns/{id}/report          | GET            | get report for a campaign                                 |
| /api/surveys                        | GET            | Find all surveys                                          |
| /api/responses/campaigns/{id}       | POST           | Add a new response to a campaign                          |
| /api/responses/{id}                 | GET/DELETE     | Find or delete response                                   |
| /api/workspaces                     | POST           | Add new workspace                                         |
| /api/workspaces/{id}/surveys        | POST           | Add new Survey to workspace                               |
| /api/workspaces/{id}/users/{userId} | POST, DELETE   | Add or remove user to/from workspace                      |
| /api/authentication                 | POST           | Username + password authentication (JWT in response body) |
| /api/users                          | POST           | register new user                                         |
| /api/users/me                       | GET            | Information about the user currently logged in            |
