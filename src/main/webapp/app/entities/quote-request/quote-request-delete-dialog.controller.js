(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('QuoteRequestDeleteController',QuoteRequestDeleteController);

    QuoteRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'QuoteRequest'];

    function QuoteRequestDeleteController($uibModalInstance, entity, QuoteRequest) {
        var vm = this;

        vm.quoteRequest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            QuoteRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
