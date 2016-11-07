(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('VacationDetailController', VacationDetailController);

    VacationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Vacation', 'Card', 'Quote', 'Passenger', 'Customer'];

    function VacationDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Vacation, Card, Quote, Passenger, Customer) {
        var vm = this;

        vm.vacation = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('elegantelephantApp:vacationUpdate', function(event, result) {
            vm.vacation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
