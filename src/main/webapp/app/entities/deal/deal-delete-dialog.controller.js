(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('DealDeleteController',DealDeleteController);

    DealDeleteController.$inject = ['$uibModalInstance', 'entity', 'Deal'];

    function DealDeleteController($uibModalInstance, entity, Deal) {
        var vm = this;

        vm.deal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Deal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
