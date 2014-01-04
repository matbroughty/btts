var bttsApp = angular.module('bttsApp', []);

//This configures the routes and associates each route with a view and a controller
bttsApp.config(function ($routeProvider) {
    $routeProvider
        .when('/',
        {
            controller: 'LeagueResultsController',
            templateUrl: 'app/partials/leagueTable.html'
        })
        .when('/players',
        {
            controller: 'PlayerResultsController',
            templateUrl: 'app/partials/playerPoints.html'
        })
        .when('/summary',
        {
            controller: 'CurrentChoicesController',
            templateUrl: 'app/partials/summaryTable.html'
        })
        .otherwise({ redirectTo: '/' });
});