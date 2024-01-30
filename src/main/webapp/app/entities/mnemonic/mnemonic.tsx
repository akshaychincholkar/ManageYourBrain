import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './mnemonic.reducer';

export const Mnemonic = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const mnemonicList = useAppSelector(state => state.mnemonic.entities);
  const loading = useAppSelector(state => state.mnemonic.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="mnemonic-heading" data-cy="MnemonicHeading">
        <Translate contentKey="manageYourBrainApp.mnemonic.home.title">Mnemonics</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="manageYourBrainApp.mnemonic.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/mnemonic/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="manageYourBrainApp.mnemonic.home.createLabel">Create new Mnemonic</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {mnemonicList && mnemonicList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="manageYourBrainApp.mnemonic.id">Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="manageYourBrainApp.mnemonic.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('creationDate')}>
                  <Translate contentKey="manageYourBrainApp.mnemonic.creationDate">Creation Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('creationDate')} />
                </th>
                <th className="hand" onClick={sort('modifiedDate')}>
                  <Translate contentKey="manageYourBrainApp.mnemonic.modifiedDate">Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('modifiedDate')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {mnemonicList.map((mnemonic, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/mnemonic/${mnemonic.id}`} color="link" size="sm">
                      {mnemonic.id}
                    </Button>
                  </td>
                  <td>{mnemonic.name}</td>
                  <td>
                    {mnemonic.creationDate ? <TextFormat type="date" value={mnemonic.creationDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {mnemonic.modifiedDate ? <TextFormat type="date" value={mnemonic.modifiedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/mnemonic/${mnemonic.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/mnemonic/${mnemonic.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/mnemonic/${mnemonic.id}/delete`)}
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
              <Translate contentKey="manageYourBrainApp.mnemonic.home.notFound">No Mnemonics found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Mnemonic;