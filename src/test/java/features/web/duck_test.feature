@web
Feature: Basic Selenium Test
  Description: This test used to test API endpoint using cucumber notation

  #scnario will be executed before each scenario
  Background: Driver is initialized
    Given Driver is initialized

  Scenario: I`m able to open DuckDuckGo page
    When I open https://duckduckgo.com/ url
    Then Tab title should contains DuckDuckGo
    And Close Driver

  Scenario: Im able to find meaning of life
    When I open https://duckduckgo.com/ url
    And I put meaning of life in a search field
    And Press search button
    Then I should find pages with meaning of life
    And Close Driver