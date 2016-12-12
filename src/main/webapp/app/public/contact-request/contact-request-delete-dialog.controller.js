(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('ContactRequestDeleteController',ContactRequestDeleteController);

    ContactRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'ContactRequest'];

    function ContactRequestDeleteController($uibModalInstance, entity, ContactRequest) {
        var vm = this;

        vm.contactRequest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ContactRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
