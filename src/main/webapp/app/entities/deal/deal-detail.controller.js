(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('DealDetailController', DealDetailController);

    DealDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Deal', 'Vendor'];

    function DealDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Deal, Vendor) {
        var vm = this;

        vm.deal = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('elegantelephantApp:dealUpdate', function(event, result) {
            vm.deal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
