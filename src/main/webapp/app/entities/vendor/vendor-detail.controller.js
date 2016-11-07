(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('VendorDetailController', VendorDetailController);

    VendorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Vendor', 'Deal'];

    function VendorDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Vendor, Deal) {
        var vm = this;

        vm.vendor = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('elegantelephantApp:vendorUpdate', function(event, result) {
            vm.vendor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
