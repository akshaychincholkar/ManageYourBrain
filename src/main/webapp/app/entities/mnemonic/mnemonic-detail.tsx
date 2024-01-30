import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mnemonic.reducer';

export const MnemonicDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mnemonicEntity = useAppSelector(state => state.mnemonic.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mnemonicDetailsHeading">
          <Translate contentKey="manageYourBrainApp.mnemonic.detail.title">Mnemonic</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="manageYourBrainApp.mnemonic.id">Id</Translate>
            </span>
          </dt>
          <dd>{mnemonicEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="manageYourBrainApp.mnemonic.name">Name</Translate>
            </span>
          </dt>
          <dd>{mnemonicEntity.name}</dd>
          <dt>
            <span id="creationDate">
              <Translate contentKey="manageYourBrainApp.mnemonic.creationDate">Creation Date</Translate>
            </span>
          </dt>
          <dd>
            {mnemonicEntity.creationDate ? <TextFormat value={mnemonicEntity.creationDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="modifiedDate">
              <Translate contentKey="manageYourBrainApp.mnemonic.modifiedDate">Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {mnemonicEntity.modifiedDate ? <TextFormat value={mnemonicEntity.modifiedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/mnemonic" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mnemonic/${mnemonicEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MnemonicDetail;
