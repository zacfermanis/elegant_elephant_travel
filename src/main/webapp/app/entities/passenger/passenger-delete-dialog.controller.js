(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('PassengerDeleteController',PassengerDeleteController);

    PassengerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Passenger'];

    function PassengerDeleteController($uibModalInstance, entity, Passenger) {
        var vm = this;

        vm.passenger = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Passenger.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
