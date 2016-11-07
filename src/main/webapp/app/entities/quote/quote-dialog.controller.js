(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('QuoteDialogController', QuoteDialogController);

    QuoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Quote', 'Passenger', 'Vacation', 'QuoteRequest'];

    function QuoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Quote, Passenger, Vacation, QuoteRequest) {
        var vm = this;

        vm.quote = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.passengers = Passenger.query();
        vm.vacations = Vacation.query();
        vm.quoterequests = QuoteRequest.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.quote.id !== null) {
                Quote.update(vm.quote, onSaveSuccess, onSaveError);
            } else {
                Quote.save(vm.quote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('elegantelephantApp:quoteUpdate', result);
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
