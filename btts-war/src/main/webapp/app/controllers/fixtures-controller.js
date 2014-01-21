bttsApp.controller('FixturesController', function ($scope, $http, $log) {
    $scope.$log = $log;


    $scope.init = function () {
        $scope.alerts = [];

        $http.get('http://btts.broughty.com/api/fixtures/').
            success(function (data) {
                $scope.fixtures = data;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.status = status;
                $scope.alerts.push({ type: 'danger', msg: 'Balls!!! Could not get fixtures.  Try again. Status Code = ' + status});
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
                    {"weekNumber":"?", "startDate":1389312000000, "endDate":1389571200000}
                ];
                $scope.selectedWeekNumber = "?";
                $scope.status = status;
                $scope.alerts.push({ type: 'danger', msg: 'Balls!!! Could not get start week for fixtures.  Try again. Status Code = ' + status});
            });



    }

    $scope.init();

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };


});
