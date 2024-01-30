import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './memory.reducer';

export const MemoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const memoryEntity = useAppSelector(state => state.memory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memoryDetailsHeading">
          <Translate contentKey="manageYourBrainApp.memory.detail.title">Memory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="manageYourBrainApp.memory.id">Id</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.id}</dd>
          <dt>
            <span id="topic">
              <Translate contentKey="manageYourBrainApp.memory.topic">Topic</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.topic}</dd>
          <dt>
            <span id="learnedDate">
              <Translate contentKey="manageYourBrainApp.memory.learnedDate">Learned Date</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.learnedDate ? <TextFormat value={memoryEntity.learnedDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="key">
              <Translate contentKey="manageYourBrainApp.memory.key">Key</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.key}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="manageYourBrainApp.memory.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.comment}</dd>
          <dt>
            <span id="creationDate">
              <Translate contentKey="manageYourBrainApp.memory.creationDate">Creation Date</Translate>
            </span>
          </dt>
          <dd>
            {memoryEntity.creationDate ? <TextFormat value={memoryEntity.creationDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="modifiedDate">
              <Translate contentKey="manageYourBrainApp.memory.modifiedDate">Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {memoryEntity.modifiedDate ? <TextFormat value={memoryEntity.modifiedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="manageYourBrainApp.memory.appUser">App User</Translate>
          </dt>
          <dd>{memoryEntity.appUser ? memoryEntity.appUser.id : ''}</dd>
          <dt>
            <Translate contentKey="manageYourBrainApp.memory.tag">Tag</Translate>
          </dt>
          <dd>{memoryEntity.tag ? memoryEntity.tag.id : ''}</dd>
          <dt>
            <Translate contentKey="manageYourBrainApp.memory.mnemonic">Mnemonic</Translate>
          </dt>
          <dd>{memoryEntity.mnemonic ? memoryEntity.mnemonic.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/memory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/memory/${memoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MemoryDetail;
