bttsApp.controller('CurrentChoicesController', function ($scope, $http, $log) {
    $scope.$log = $log;

    $scope.init = function () {
        $scope.choice;
        $scope.choice1;
        $scope.choice2;
        $scope.choice3;
        $scope.choice4;
        $scope.alerts = [];

        $http.get('http://btts.broughty.com/api/fixtures/').
            success(function (data) {
                $scope.fixtures = data;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.status = status;
                $scope.alerts.push({ type:'danger', msg:'Balls!!! Could not get fixtures.  Try again. Status Code = ' + status});
            });

        $http.get('http://btts.broughty.com/api/fixtures/simple').
            success(function (data) {
                $scope.fixturesSimple = data;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.fixturesSimple = ['Arsenal', 'Arsenal', 'Arsenal', 'Arsenal'];
                $scope.status = status;
                $scope.alerts.push({ type:'danger', msg:'Balls!!! Could not get fixtures.  Try again. Status Code = ' + status});
            });

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


        $scope.getFixtures();

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
                    {"dateEntered":null, "week":"unknown", "player":"Error", "choice1":"", "choice2":"", "choice3":"", "choice4":"", "choice1Result":"WAITING", "choice2Result":"FAIL", "choice3Result":"FAIL", "choice4Result":"FAIL", "choice1Points":0, "choice2Points":0, "choice3Points":0, "choice4Points":0, "alerted":false, "defaultChoices":false}
                ];
                $scope.status = status;
            });

    }

    $scope.getFixtures = function () {
        $http.get('http://btts.broughty.com/api/fixtures/').
            success(function (data) {
                $scope.fixtures = data;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
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


    $scope.getPlayerChoices = function () {

        if($scope.selectedWeekNumber == null){
            return;
        }

        if($scope.choice == null || $scope.choice.playerName == null){
            return;
        }

        var url = 'http://btts.broughty.com/api/choices/' + String($scope.selectedWeekNumber) + '/' + String($scope.choice.playerName.name);
        $log.info("URL getting player choices from = " + url);


        $http.get(url).
            success(function (data) {
                $scope.existingChoices = data;
                $scope.choice1 = $scope.existingChoices.choice1;
                $scope.choice2 = $scope.existingChoices.choice2;
                $scope.choice3 = $scope.existingChoices.choice3;
                $scope.choice4 = $scope.existingChoices.choice4;
            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.alerts.push({ type:'info', msg:'Problem getting player choices (have you made a choice yet?). Status = ' + status});
                $scope.status = status;
                $scope.choice1 = null;
                $scope.choice2 = null;
                $scope.choice3 = null;
                $scope.choice4 = null;
            });


    }


    $scope.postChoices = function () {

        if (typeof $scope.choice1 === 'undefined' || null == $scope.choice2 || null == $scope.choice3 || null == $scope.choice4 || null == $scope.choice.playerName) {
            $scope.alerts.push({ type:'danger', msg:'Make a full selection!  Try again you plonker.' + status});
            return;
        }


        var url = 'http://btts.broughty.com/choices?player=' + String($scope.choice.playerName.name) + '&choice1=' + $scope.choice1 + '&choice2=' + $scope.choice2 + '&choice3=' + $scope.choice3 + '&choice4=' + $scope.choice4 + '&week=' + $scope.selectedWeekNumber;
        $log.info("URL posting choices to = " + url);

        $http({
            method:'POST',
            url:url,
            contentType:false,
            processData:false,
            headers:{
                'Content-Type':'application/x-www-form-urlencoded'
            }
        }).success(function (data) {
                $scope.alerts.push({ type:'success', msg:'Well done! You successfully posted your choices for week number ' + $scope.selectedWeekNumber});

            }).
            error(function (data, status, headers, config) {
                $log.log("Failed because status " + status + " and with data " + data);
                $log.log("Failed because headers " + headers + " and with config " + config);
                $scope.currentChoices = [
                    {"dateEntered":null, "week":"unknown", "player":"Error", "choice1":"", "choice2":"", "choice3":"", "choice4":"", "choice1Result":"WAITING", "choice2Result":"FAIL", "choice3Result":"FAIL", "choice4Result":"FAIL", "choice1Points":0, "choice2Points":0, "choice3Points":0, "choice4Points":0, "alerted":false, "defaultChoices":false}
                ];
                $scope.status = status;
                $scope.alerts.push({ type:'danger', msg:'Uh oh Choices post failed.  Try again. Status Code = ' + status});

            });
    }


    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.showPlayer = function (playerName) {
        window.location = '#/players/' + playerName;
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
                {"dateEntered":null, "week":"unknown", "player":"Error", "choice1":"", "choice2":"", "choice3":"", "choice4":"", "choice1Result":"WAITING", "choice2Result":"FAIL", "choice3Result":"FAIL", "choice4Result":"FAIL", "choice1Points":0, "choice2Points":0, "choice3Points":0, "choice4Points":0, "alerted":false, "defaultChoices":false}
            ];
            $scope.status = status;
        });
});