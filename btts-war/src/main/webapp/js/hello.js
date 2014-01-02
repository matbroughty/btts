/**
 * Created by matbroughty on 01/01/14.
 */
function Hello($scope, $http) {
    $http.get('http://btts.broughty.com/rest/league/Mat').
        success(function (data) {
            $scope.greeting = data;
        });

}


