(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('CardDeleteController',CardDeleteController);

    CardDeleteController.$inject = ['$uibModalInstance', 'entity', 'Card'];

    function CardDeleteController($uibModalInstance, entity, Card) {
        var vm = this;

        vm.card = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Card.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
