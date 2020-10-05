Feature: Basic Requests API Test
  Description: This test used to test API endpoint using cucumber notation

  Scenario: API Client is initialized
    Given API Client is initialized

  Scenario: Im able to get all users
    When I execute GET users request
    Then I should receive 200 response code

  Scenario: Im able to get user 1
    When I execute GET users/1 request
    Then I should receive 200 response code
    And Response body is matched to schema.json

  Scenario: Im able to get all albums
    When I execute GET albums request
    Then I should receive 200 response code

  Scenario: Im able to get all posts
    When I execute GET posts request
    Then I should receive 200 response code

  Scenario Outline: System should have clear response codes
    When I execute GET <endpoint> request
    Then I should receive <code> response code

    Examples:
      | endpoint   | code |
      | users      | 200  |
      | notusersds | 404  |
      | albums     | 200  |
      | blblb      | 404  |

  Scenario: GET posts should return rc 404
    When I execute GET posts/invalid_id request
    Then I should receive 404 response code

  Scenario: GET users  should return rc 404
    When I execute GET users/invalid_id request
    Then I should receive 404 response code