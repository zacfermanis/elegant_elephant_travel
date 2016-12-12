(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('public', {
            abstract: true,
            parent: 'app'
        });
    }
})();
