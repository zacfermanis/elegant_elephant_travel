(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('ContactRequestFinishController',ContactRequestFinishController);

    ContactRequestFinishController.$inject = ['$uibModalInstance', 'ContactRequest'];

    function ContactRequestFinishController($uibModalInstance, ContactRequest) {
        var vm = this;

        vm.clear = clear;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
