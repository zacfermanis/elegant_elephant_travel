(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('PassengerDialogController', PassengerDialogController);

    PassengerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Passenger', 'QuoteRequest', 'Quote', 'Vacation'];

    function PassengerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Passenger, QuoteRequest, Quote, Vacation) {
        var vm = this;

        vm.passenger = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.quoterequests = QuoteRequest.query();
        vm.quotes = Quote.query();
        vm.vacations = Vacation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.passenger.id !== null) {
                Passenger.update(vm.passenger, onSaveSuccess, onSaveError);
            } else {
                Passenger.save(vm.passenger, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('elegantelephantApp:passengerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateOfBirth = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
