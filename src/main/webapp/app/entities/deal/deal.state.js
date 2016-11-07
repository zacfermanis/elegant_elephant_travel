(function() {
    'use strict';

    angular
        .module('elegantelephantApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('deal', {
            parent: 'entity',
            url: '/deal',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Deals'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deal/deals.html',
                    controller: 'DealController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('deal-detail', {
            parent: 'entity',
            url: '/deal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Deal'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/deal/deal-detail.html',
                    controller: 'DealDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Deal', function($stateParams, Deal) {
                    return Deal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'deal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('deal-detail.edit', {
            parent: 'deal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deal/deal-dialog.html',
                    controller: 'DealDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deal', function(Deal) {
                            return Deal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deal.new', {
            parent: 'deal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deal/deal-dialog.html',
                    controller: 'DealDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                headLine: null,
                                description: null,
                                startingPrice: null,
                                highPrice: null,
                                image: null,
                                imageContentType: null,
                                url: null,
                                startDate: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('deal', null, { reload: 'deal' });
                }, function() {
                    $state.go('deal');
                });
            }]
        })
        .state('deal.edit', {
            parent: 'deal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deal/deal-dialog.html',
                    controller: 'DealDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Deal', function(Deal) {
                            return Deal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deal', null, { reload: 'deal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('deal.delete', {
            parent: 'deal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/deal/deal-delete-dialog.html',
                    controller: 'DealDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Deal', function(Deal) {
                            return Deal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('deal', null, { reload: 'deal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
