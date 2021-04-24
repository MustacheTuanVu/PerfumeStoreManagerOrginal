import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './day-works.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDayWorksDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DayWorksDetail = (props: IDayWorksDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { dayWorksEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dayWorksDetailsHeading">
          <Translate contentKey="perfumeStoreManagerApp.dayWorks.detail.title">DayWorks</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dayWorksEntity.id}</dd>
          <dt>
            <span id="workID">
              <Translate contentKey="perfumeStoreManagerApp.dayWorks.workID">Work ID</Translate>
            </span>
          </dt>
          <dd>{dayWorksEntity.workID}</dd>
          <dt>
            <span id="userID">
              <Translate contentKey="perfumeStoreManagerApp.dayWorks.userID">User ID</Translate>
            </span>
          </dt>
          <dd>{dayWorksEntity.userID}</dd>
          <dt>
            <span id="dayWork">
              <Translate contentKey="perfumeStoreManagerApp.dayWorks.dayWork">Day Work</Translate>
            </span>
          </dt>
          <dd>{dayWorksEntity.dayWork}</dd>
          <dt>
            <Translate contentKey="perfumeStoreManagerApp.dayWorks.member">Member</Translate>
          </dt>
          <dd>{dayWorksEntity.member ? dayWorksEntity.member.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/day-works" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/day-works/${dayWorksEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ dayWorks }: IRootState) => ({
  dayWorksEntity: dayWorks.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DayWorksDetail);
