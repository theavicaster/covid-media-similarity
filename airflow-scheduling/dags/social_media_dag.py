from datetime import timedelta
from textwrap import dedent

from airflow import DAG

from airflow.operators.bash import BashOperator
from airflow.utils.dates import days_ago

default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'email': ['airflow@example.com'],
    'start_date': days_ago(0),
    'retries': 1,
    'retry_delay': timedelta(minutes=1),
    'email_on_failure': False,
    'email_on_retry': False,
}

dag = DAG(
    dag_id='social_media_dag',
    default_args=default_args,
    description='Calculates similarities for Social Media text',
    schedule_interval=timedelta(minutes=10),
    catchup=False
)

t1 = BashOperator(
  task_id='load_text_from_apis',
  bash_command='java -jar /home/avinandan/social-media-similarity/spring-rest/spring-consumer-0.0.1-SNAPSHOT.jar',
  dag=dag
)

t2 = BashOperator(
  task_id='process_text_in_spark',
  bash_command='$SPARK_HOME/bin/spark-submit --class text.similarity.processing.SparkTextSimilarity --master local[*] /home/avinandan/social-media-similarity/spark-nlp/spark-nlp-assembly-0.1.jar',
  dag=dag
)

t1 >> t2