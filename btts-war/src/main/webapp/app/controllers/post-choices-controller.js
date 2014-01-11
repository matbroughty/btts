/**
 * Created by matbroughty on 08/01/14.
 */
bttsApp.controller('PostChoicesController', function ($scope, $http, $log) {
    $scope.$log = $log;

    this.addChoice = function (choice) {
        $http.post("http://btts.broughty.com/api/choices", choice)
            .success(function (data) {
                this.choice = data;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
            });
    }

});