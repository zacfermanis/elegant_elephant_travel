(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('QuoteDetailController', QuoteDetailController);

    QuoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Quote', 'Passenger', 'Vacation', 'QuoteRequest'];

    function QuoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Quote, Passenger, Vacation, QuoteRequest) {
        var vm = this;

        vm.quote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('elegantelephantApp:quoteUpdate', function(event, result) {
            vm.quote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
