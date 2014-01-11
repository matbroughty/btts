bttsApp.controller('CurrentChoicesController', function ($scope, $http, $log) {
    $scope.$log = $log;

    $scope.choice;
    $scope.init = function () {

        $scope.alerts = [];

        $http.get('http://btts.broughty.com/api/choices').
            success(function (data) {
                $scope.currentChoices = data;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.currentChoices = [
                    {"dateEntered": null, "week": "unknown", "player": "Error", "choice1": "", "choice2": "", "choice3": "", "choice4": "", "choice1Result": "WAITING", "choice2Result": "FAIL", "choice3Result": "FAIL", "choice4Result": "FAIL", "choice1Points": 0, "choice2Points": 0, "choice3Points": 0, "choice4Points": 0, "alerted": false, "defaultChoices": false}
                ];
                $scope.status = status;
            });

        $http.get('http://btts.broughty.com/api/').
            success(function (data) {
                $scope.currentWeek = data;
                $scope.selectedWeekNumber = $scope.currentWeek.weekNumber;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.currentWeek = [
                    {"weekNumber":"22","startDate":1389312000000,"endDate":1389571200000}
                ];
                $scope.selectedWeekNumber = "22";
                $scope.status = status;
            });

        $http.get('http://btts.broughty.com/api/players/').
            success(function (data) {
                $scope.players = data;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.status = status;
            });


        $scope.weeks = ["18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"];

    }


    $scope.update = function () {
        $http.get('http://btts.broughty.com/api/choices/' + $scope.selectedWeekNumber).
            success(function (data) {
                $scope.currentChoices = data;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.currentChoices = [
                    {"dateEntered": null, "week": "unknown", "player": "Error", "choice1": "", "choice2": "", "choice3": "", "choice4": "", "choice1Result": "WAITING", "choice2Result": "FAIL", "choice3Result": "FAIL", "choice4Result": "FAIL", "choice1Points": 0, "choice2Points": 0, "choice3Points": 0, "choice4Points": 0, "alerted": false, "defaultChoices": false}
                ];
                $scope.status = status;
            });

    }



    $scope.awesome = function (choice) {
        if (choice === 'WAITING'.valueOf()) {
            return 'fa fa-question-circle'.valueOf();
        }
        if (choice === 'FAIL'.valueOf()) {
            return 'fa fa-thumbs-down'.valueOf();
        }
        return 'fa fa-thumbs-up'.valueOf();
    }


    $scope.postChoices = function () {
        $log.info($scope.choice.choice1);
        $scope.alerts.push({ type: 'info', msg: 'Well done! You successfully posted your choices.'});
    }


    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.init();



});
bttsApp.controller('CurrentStarChoicesController', function ($scope, $http, $log) {
    $scope.$log = $log;
    $http.get('http://btts.broughty.com/api/choices/Star').
        success(function (data) {
            $scope.currentChoices = data;
        }).
        error(function (data, status, headers, config) {
            $log.log("Failed because status " + status + " and with data " + data);
            $log.log("Failed because headers " + headers + " and with config " + config);
            $scope.currentChoices = [
                {"dateEntered": null, "week": "unknown", "player": "Error", "choice1": "", "choice2": "", "choice3": "", "choice4": "", "choice1Result": "WAITING", "choice2Result": "FAIL", "choice3Result": "FAIL", "choice4Result": "FAIL", "choice1Points": 0, "choice2Points": 0, "choice3Points": 0, "choice4Points": 0, "alerted": false, "defaultChoices": false}
            ];
            $scope.status = status;
        });
});