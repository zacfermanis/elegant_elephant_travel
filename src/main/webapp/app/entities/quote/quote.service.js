(function() {
    'use strict';
    angular
        .module('elegantelephantApp')
        .factory('Quote', Quote);

    Quote.$inject = ['$resource', 'DateUtils'];

    function Quote ($resource, DateUtils) {
        var resourceUrl =  'api/quotes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.departureDate = DateUtils.convertDateTimeFromServer(data.departureDate);
                        data.returnDate = DateUtils.convertDateTimeFromServer(data.returnDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
