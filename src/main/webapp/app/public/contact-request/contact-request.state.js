(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contact-request', {
            parent: 'home',
            url: '/contact-request',
            data: {
                authorities: [],
                pageTitle: 'ContactRequests'
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/contact-request/contact-requests.html',
                    controller: 'ContactRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('contact-request-detail', {
            parent: 'home',
            url: '/contact-request/{id}',
            data: {
                authorities: [],
                pageTitle: 'ContactRequest'
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/contact-request/contact-request-detail.html',
                    controller: 'ContactRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ContactRequest', function($stateParams, ContactRequest) {
                    return ContactRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'contact-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('contact-request-detail.edit', {
            parent: 'contact-request-detail',
            url: '/detail/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/public/contact-request/contact-request-dialog.html',
                    controller: 'ContactRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContactRequest', function(ContactRequest) {
                            return ContactRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-request.new', {
            parent: 'contact-request',
            url: '/new',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/public/contact-request/contact-request-dialog.html',
                    controller: 'ContactRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                contactPhone: null,
                                preferredContactMethod: null,
                                requestDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contact-request', null, { reload: 'contact-request' });
                }, function() {
                    $state.go('contact-request');
                });
            }]
        })
        .state('contact-request.create', {
            parent: 'home',
            url: '/create',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/public/contact-request/contact-request-dialog.html',
                    controller: 'ContactRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                contactPhone: null,
                                preferredContactMethod: null,
                                requestDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                        $state.go('contact-request.finish', null, { reload: 'contact-request.finish' });
                    }, function() {
                        $state.go('contact-request');
                    });
            }]
        })
        .state('contact-request.edit', {
            parent: 'contact-request',
            url: '/{id}/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/public/contact-request/contact-request-dialog.html',
                    controller: 'ContactRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContactRequest', function(ContactRequest) {
                            return ContactRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-request', null, { reload: 'contact-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-request.delete', {
            parent: 'contact-request',
            url: '/{id}/delete',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/public/contact-request/contact-request-delete-dialog.html',
                    controller: 'ContactRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ContactRequest', function(ContactRequest) {
                            return ContactRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact-request', null, { reload: 'contact-request' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact-request.finish', {
            parent: 'home',
            url: '/finish',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/public/contact-request/contact-request-finish.html',
                    controller: 'ContactRequestFinishController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                    }
                }).result.then(function() {
                        $state.go('home', null, { reload: 'home' });
                    }, function() {
                        $state.go('^');
                    });
            }]
        });
    }

})();
