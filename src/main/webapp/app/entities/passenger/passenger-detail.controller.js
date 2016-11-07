(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('PassengerDetailController', PassengerDetailController);

    PassengerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Passenger', 'QuoteRequest', 'Quote', 'Vacation'];

    function PassengerDetailController($scope, $rootScope, $stateParams, previousState, entity, Passenger, QuoteRequest, Quote, Vacation) {
        var vm = this;

        vm.passenger = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('elegantelephantApp:passengerUpdate', function(event, result) {
            vm.passenger = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
