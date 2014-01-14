bttsApp.controller('MaintenanceController', function ($scope, $http, $log) {
    $scope.$log = $log;


    $scope.init = function () {

        $scope.newWeek = null;
        $scope.newStartDate = null;
        $scope.newEndDate = null;

        $scope.alerts = [];

        $http.get('http://btts.broughty.com/api/').
            success(function (data) {
                $scope.currentWeek = data;
                $scope.selectedWeekNumber = $scope.currentWeek.weekNumber;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.currentWeek = [
                    {"weekNumber":"22", "startDate":1389312000000, "endDate":1389571200000}
                ];
                $scope.selectedWeekNumber = "22";
                $scope.status = status;
            });


    }

    $scope.init();


    $scope.refreshResults = function () {

        var url = 'http://btts.broughty.com/fixtures?mobile=refreshOnLine';
        $log.info("URL posting refreshResults to = " + url);

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

    $scope.refreshReminder = function () {

        var url = 'http://btts.broughty.com/reminders?mobile=refreshOnLine';
        $log.info("URL posting refreshReminder to = " + url);

        $http({
            method: 'POST',
            url: url,
            contentType: false,
            processData: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).success(function (data) {
                $scope.alerts.push({ type: 'success', msg: 'You successfully sent the reminder emails.'});

            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.status = status;
                $scope.alerts.push({ type: 'danger', msg: 'Uh oh reminder email failed.  Try again. Status Code = ' + status});

            });

    }

    $scope.refreshFinalSelections = function () {

        var url = 'http://btts.broughty.com/selections?mobile=refreshOnLine';
        $log.info("URL posting refreshFinalSelections to = " + url);

        $http({
            method: 'POST',
            url: url,
            contentType: false,
            processData: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).success(function (data) {
                $scope.alerts.push({ type: 'success', msg: 'You successfully sent the final selections emails.'});

            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.status = status;
                $scope.alerts.push({ type: 'danger', msg: 'Uh oh final selections email failed.  Try again. Status Code = ' + status});

            });

    }

    $scope.refreshResults = function () {

        var url = 'http://btts.broughty.com/emailresults?mobile=refreshOnLine';
        $log.info("URL posting refreshResults to = " + url);

        $http({
            method: 'POST',
            url: url,
            contentType: false,
            processData: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).success(function (data) {
                $scope.alerts.push({ type: 'success', msg: 'You successfully sent the results emails.'});

            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.status = status;
                $scope.alerts.push({ type: 'danger', msg: 'Uh oh results email failed.  Try again. Status Code = ' + status});

            });

    }


    $scope.updateWeek = function () {

        var url = 'http://btts.broughty.com/maintenance?current_week=' + $scope.newWeek + "&start_date=" + $scope.newStartDate + "&end_date=" + $scope.newEndDate;
        $log.info("URL posting updateWeek to = " + url);

        if($scope.newWeek == null || $scope.newStartDate || $scope.newStartDate){
            $scope.alerts.push({ type: 'danger', msg: 'What madness are you trying to inflict?'});
        }

        $http({
            method: 'POST',
            url: url,
            contentType: false,
            processData: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).success(function (data) {
                $scope.alerts.push({ type: 'success', msg: 'You successfully updated the current week.'});

            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.status = status;
                $scope.alerts.push({ type: 'danger', msg: 'Uh oh update of week failed.  Try again. Status Code = ' + status});
            });

        $scope.alerts.push({ type: 'success', msg: 'You successfully updated the current week.'});

    }

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };



});
