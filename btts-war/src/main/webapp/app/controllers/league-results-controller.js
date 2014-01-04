bttsApp.controller('LeagueResultsController', function ($scope, $http, $log) {
    $scope.$log = $log;
    delete $http.defaults.headers.common['X-Requested-With'];
    $http.get('http://btts.broughty.com/api/league').
        success(function (data) {
            $scope.allScores = data;
        }).
        error(function (data, status, headers, config) {
            $log.log("Failed because status " + status + " and with data " + data);
            $log.log("Failed because headers " + headers + " and with config " + config);
            $scope.allScores = [
                {'playerName': 'Error', 'points': [0], 'total': 0}
            ];
            $scope.status = status;
        });
});

bttsApp.controller('PlayerResultsController', function ($scope, $http) {

    delete $http.defaults.headers.common['X-Requested-With'];
    $http.get('http://btts.broughty.com/api/league/Mat').
        success(function (data) {
            $scope.playerScores = data;
        });

    //$scope.playerScores=leagueResultsFactory.getIndividualResults();
});