(function() {
    'use strict';
    angular
        .module('elegantelephantApp')
        .factory('ContactRequest', ContactRequest);

    ContactRequest.$inject = ['$resource', 'DateUtils'];

    function ContactRequest ($resource, DateUtils) {
        var resourceUrl =  'api/contact-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.requestDate = DateUtils.convertDateTimeFromServer(data.requestDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
