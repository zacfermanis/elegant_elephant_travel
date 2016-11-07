(function() {
    'use strict';
    angular
        .module('elegantelephantApp')
        .factory('QuoteRequest', QuoteRequest);

    QuoteRequest.$inject = ['$resource', 'DateUtils'];

    function QuoteRequest ($resource, DateUtils) {
        var resourceUrl =  'api/quote-requests/:id';

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
