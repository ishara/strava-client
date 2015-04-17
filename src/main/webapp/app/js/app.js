var stravaApp = angular.module('stravaApp', [
    'ngRoute',
    'stravaControllers'
]);

stravaApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/activities', {
                templateUrl: 'app/templates/activities.html',
                controller: 'ActivitiesCtrl'
            }).
            otherwise({
                redirectTo: '/activities'
            });
    }]);