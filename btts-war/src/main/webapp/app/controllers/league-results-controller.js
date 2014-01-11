bttsApp.controller('LeagueResultsController', function ($scope, $http, $log) {
    $scope.$log = $log;


    $scope.leagueGraph = function (league) {


    }

    $scope.init = function () {
        $http.get('http://btts.broughty.com/api/league').
            success(function (data) {
                $scope.allScores = data;
                if($scope.allScores != null){
                    var svg = dimple.newSvg("#graphit", 800, 600);
                    var data = $scope.allScores;
                    var myChart = new dimple.chart(svg, data);
                    var x = myChart.addCategoryAxis("x", "playerName");
                    var y = myChart.addMeasureAxis("y", "total");

                    var s = myChart.addSeries(null, dimple.plot.bar);

                    s.barGap = 0.05;

                    myChart.draw();
                }
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.allScores = [
                    {'playerName': 'Error', 'points': [0], 'total': 0}
                ];
                $scope.status = status;
            });



    }

    $scope.init();

});

bttsApp.controller('PlayerResultsController', function ($scope, $http) {

    delete $http.defaults.headers.common['X-Requested-With'];
    $http.get('http://btts.broughty.com/api/league/Mat').
        success(function (data) {
            $scope.playerScores = data;
        });

    //$scope.playerScores=leagueResultsFactory.getIndividualResults();
});