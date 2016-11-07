(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('VendorDeleteController',VendorDeleteController);

    VendorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Vendor'];

    function VendorDeleteController($uibModalInstance, entity, Vendor) {
        var vm = this;

        vm.vendor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Vendor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
