(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('ContactRequestDialogController', ContactRequestDialogController);

    ContactRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ContactRequest'];

    function ContactRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ContactRequest) {
        var vm = this;

        vm.contactRequest = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contactRequest.id !== null) {
                ContactRequest.update(vm.contactRequest, onSaveSuccess, onSaveError);
            } else {
                ContactRequest.save(vm.contactRequest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('elegantelephantApp:contactRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.requestDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
