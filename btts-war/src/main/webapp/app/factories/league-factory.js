/**
 * Created by matbroughty on 03/01/14.
 */
bttsApp.factory('leagueResultsFactory', ['$http', function ($http) {
    var factory = {};
    var results = [{'playerName':'Dave','points':[12], 'total':12},{'playerName':'Frank','points':[11], 'total':11},{'playerName':'Mat','points':[14], 'total':14}];
    var individual = {'playerName':'Dave','points':[12, 14, 5], 'total':31};
    factory.getLeagueResults = function(){
        $http.get('http://btts.broughty.com/rest/league').
            success(function (data) {
                results = data;
            });
        return results;
    };

    factory.getIndividualResults = function(){
        $http.get('http://btts.broughty.com/rest/league/Mat').
            success(function (data) {
                individual = data;
            });
        return individual;
    };

    return factory;

}]);