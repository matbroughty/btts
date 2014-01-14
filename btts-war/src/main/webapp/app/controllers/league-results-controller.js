bttsApp.controller('LeagueResultsController', function ($scope, $http, $log) {
    $scope.$log = $log;


    $scope.leagueGraph = function (league) {


    }

    $scope.showPlayer = function (playerName) {
        window.location = '#/players/' + playerName;
    };

    $scope.init = function () {

        $scope.alerts = [];

        $http.get('http://btts.broughty.com/api/league').
            success(function (data) {
                $scope.allScores = data;
                if ($scope.allScores != null) {
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


    $scope.refreshResults = function () {

        var url = 'http://btts.broughty.com/fixtures?mobile=refreshOnLine';
        $log.info("URL posting choices to = " + url);

        $http({
            method: 'POST',
            url: url,
            contentType: false,
            processData: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).success(function (data) {
                $scope.alerts.push({ type: 'success', msg: 'You successfully initiated a scan of the latest results.  Refresh the browser in a minute or so.'});

            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.status = status;
                $scope.alerts.push({ type: 'danger', msg: 'Uh oh Scan of latest results failed.  Try again. Status Code = ' + status});

            });

    }

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };


});

bttsApp.controller('PlayerResultsController', function ($scope, $http, $routeParams) {


    $scope.awesome = function (choice) {
        if (choice === 'WAITING'.valueOf()) {
            return 'fa fa-question-circle'.valueOf();
        }
        if (choice === 'FAIL'.valueOf()) {
            return 'fa fa-thumbs-down'.valueOf();
        }
        return 'fa fa-thumbs-up'.valueOf();
    }


    $scope.playerName = $routeParams.playerName;


    if ($scope.playerName != null) {

        $http.get('http://btts.broughty.com/api/choices/player/' + $scope.playerName).
            success(function (data) {
                $scope.playerChoices = data;
            }).
            error(function (data, status, headers, config) {
                $scope.playerChoices = [
                    {"dateEntered": null, "week": "unknown", "player": "Error", "choice1": "", "choice2": "", "choice3": "", "choice4": "", "choice1Result": "WAITING", "choice2Result": "FAIL", "choice3Result": "FAIL", "choice4Result": "FAIL", "choice1Points": 0, "choice2Points": 0, "choice3Points": 0, "choice4Points": 0, "alerted": false, "defaultChoices": false}
                ];
                $scope.status = status;
            });
    }

})
;