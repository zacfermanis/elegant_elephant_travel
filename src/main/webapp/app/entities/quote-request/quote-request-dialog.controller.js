(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('QuoteRequestDialogController', QuoteRequestDialogController);

    QuoteRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'QuoteRequest', 'Passenger', 'Quote', 'Customer'];

    function QuoteRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, QuoteRequest, Passenger, Quote, Customer) {
        var vm = this;

        vm.quoteRequest = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.passengers = Passenger.query();
        vm.quotes = Quote.query();
        vm.customers = Customer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.quoteRequest.id !== null) {
                QuoteRequest.update(vm.quoteRequest, onSaveSuccess, onSaveError);
            } else {
                QuoteRequest.save(vm.quoteRequest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('elegantelephantApp:quoteRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.departureDate = false;
        vm.datePickerOpenStatus.returnDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
