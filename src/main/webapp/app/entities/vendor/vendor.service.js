(function() {
    'use strict';
    angular
        .module('elegantelephantApp')
        .factory('Vendor', Vendor);

    Vendor.$inject = ['$resource'];

    function Vendor ($resource) {
        var resourceUrl =  'api/vendors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
