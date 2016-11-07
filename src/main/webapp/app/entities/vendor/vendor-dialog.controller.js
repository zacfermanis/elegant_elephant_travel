(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .controller('VendorDialogController', VendorDialogController);

    VendorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Vendor', 'Deal'];

    function VendorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Vendor, Deal) {
        var vm = this;

        vm.vendor = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.deals = Deal.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vendor.id !== null) {
                Vendor.update(vm.vendor, onSaveSuccess, onSaveError);
            } else {
                Vendor.save(vm.vendor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('elegantelephantApp:vendorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setLogo = function ($file, vendor) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        vendor.logo = base64Data;
                        vendor.logoContentType = $file.type;
                    });
                });
            }
        };

    }
})();
