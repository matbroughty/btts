/**
 * Created by matbroughty on 01/01/14.
 */
function Hello($scope, $http) {
    $http.get('http://localhost:8080/rest/league/Mat').
        success(function (data) {
            $scope.greeting = data;
        });

}


