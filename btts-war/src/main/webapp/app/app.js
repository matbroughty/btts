var bttsApp = angular.module('bttsApp', ['ngRoute', 'ui.bootstrap']);


//This configures the routes and associates each route with a view and a controller
bttsApp.config(function ($routeProvider) {
    $routeProvider
        .when('/',
        {
            controller:'LeagueResultsController',
            templateUrl:'app/partials/leagueTable.html'
        })
        .when('/players/:playerName',
        {
            controller:'PlayerResultsController',
            templateUrl:'app/partials/playerPoints.html'
        })
        .when('/choices',
        {
            controller:'CurrentChoicesController',
            templateUrl:'app/partials/choices.html'
        })
        .when('/summary',
        {
            controller:'CurrentChoicesController',
            templateUrl:'app/partials/summaryTable.html'
        })
        .otherwise({ redirectTo:'/' });
});
