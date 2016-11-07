(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('CustomerDetailController', CustomerDetailController);

    CustomerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Customer', 'Address', 'Vacation', 'QuoteRequest'];

    function CustomerDetailController($scope, $rootScope, $stateParams, previousState, entity, Customer, Address, Vacation, QuoteRequest) {
        var vm = this;

        vm.customer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('elegantelephantApp:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
