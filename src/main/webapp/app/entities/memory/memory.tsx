import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './memory.reducer';

export const Memory = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const memoryList = useAppSelector(state => state.memory.entities);
  const loading = useAppSelector(state => state.memory.loading);
  const links = useAppSelector(state => state.memory.links);
  const updateSuccess = useAppSelector(state => state.memory.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="memory-heading" data-cy="MemoryHeading">
        <Translate contentKey="manageYourBrainApp.memory.home.title">Memories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="manageYourBrainApp.memory.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/memory/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="manageYourBrainApp.memory.home.createLabel">Create new Memory</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={memoryList ? memoryList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {memoryList && memoryList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="manageYourBrainApp.memory.id">Id</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('topic')}>
                    <Translate contentKey="manageYourBrainApp.memory.topic">Topic</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('topic')} />
                  </th>
                  <th className="hand" onClick={sort('learnedDate')}>
                    <Translate contentKey="manageYourBrainApp.memory.learnedDate">Learned Date</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('learnedDate')} />
                  </th>
                  <th className="hand" onClick={sort('key')}>
                    <Translate contentKey="manageYourBrainApp.memory.key">Key</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('key')} />
                  </th>
                  <th className="hand" onClick={sort('comment')}>
                    <Translate contentKey="manageYourBrainApp.memory.comment">Comment</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('comment')} />
                  </th>
                  <th className="hand" onClick={sort('creationDate')}>
                    <Translate contentKey="manageYourBrainApp.memory.creationDate">Creation Date</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('creationDate')} />
                  </th>
                  <th className="hand" onClick={sort('modifiedDate')}>
                    <Translate contentKey="manageYourBrainApp.memory.modifiedDate">Modified Date</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('modifiedDate')} />
                  </th>
                  <th>
                    <Translate contentKey="manageYourBrainApp.memory.appUser">App User</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="manageYourBrainApp.memory.tag">Tag</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="manageYourBrainApp.memory.mnemonic">Mnemonic</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {memoryList.map((memory, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/memory/${memory.id}`} color="link" size="sm">
                        {memory.id}
                      </Button>
                    </td>
                    <td>{memory.topic}</td>
                    <td>{memory.learnedDate ? <TextFormat type="date" value={memory.learnedDate} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{memory.key}</td>
                    <td>{memory.comment}</td>
                    <td>{memory.creationDate ? <TextFormat type="date" value={memory.creationDate} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{memory.modifiedDate ? <TextFormat type="date" value={memory.modifiedDate} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{memory.appUser ? <Link to={`/app-user/${memory.appUser.id}`}>{memory.appUser.id}</Link> : ''}</td>
                    <td>{memory.tag ? <Link to={`/tag/${memory.tag.id}`}>{memory.tag.id}</Link> : ''}</td>
                    <td>{memory.mnemonic ? <Link to={`/mnemonic/${memory.mnemonic.id}`}>{memory.mnemonic.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/memory/${memory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/memory/${memory.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/memory/${memory.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="manageYourBrainApp.memory.home.notFound">No Memories found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Memory;
