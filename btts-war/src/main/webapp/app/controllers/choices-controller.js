bttsApp.controller('CurrentChoicesController', function ($scope, $http, $log) {
    $scope.$log = $log;
    $http.get('http://btts.broughty.com/api/choices').
        success(function (data) {
            $scope.currentChoices = data;
        }).
        error(function (data, status, headers, config) {
            $log.log("Failed because status " + status + " and with data " + data);
            $log.log("Failed because headers " + headers + " and with config " + config);
            $scope.currentChoices = [
                {"dateEntered":null, "week":"unknown", "player":"Error", "choice1":"", "choice2":"", "choice3":"", "choice4":"", "choice1Result":"WAITING", "choice2Result":"FAIL", "choice3Result":"FAIL", "choice4Result":"FAIL", "choice1Points":0, "choice2Points":0, "choice3Points":0, "choice4Points":0, "alerted":false, "defaultChoices":false}
            ];
            $scope.status = status;
        });

    $scope.awesome = function (choice) {
        if (choice === 'WAITING'.valueOf()) {
            return 'fa fa-question-circle'.valueOf();
        }
        if (choice === 'FAIL'.valueOf()) {
            return 'fa fa-thumbs-down'.valueOf();
        }
        return 'fa fa-thumbs-up'.valueOf();
    }
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
                {"dateEntered":null, "week":"unknown", "player":"Error", "choice1":"", "choice2":"", "choice3":"", "choice4":"", "choice1Result":"WAITING", "choice2Result":"FAIL", "choice3Result":"FAIL", "choice4Result":"FAIL", "choice1Points":0, "choice2Points":0, "choice3Points":0, "choice4Points":0, "alerted":false, "defaultChoices":false}
            ];
            $scope.status = status;
        });
});