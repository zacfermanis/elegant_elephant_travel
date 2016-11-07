(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('QuoteRequestDetailController', QuoteRequestDetailController);

    QuoteRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'QuoteRequest', 'Passenger', 'Quote', 'Customer'];

    function QuoteRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, QuoteRequest, Passenger, Quote, Customer) {
        var vm = this;

        vm.quoteRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('elegantelephantApp:quoteRequestUpdate', function(event, result) {
            vm.quoteRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
