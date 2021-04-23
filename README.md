# COVID-19 Social Media Text Similarity

Periodically compares recent tweets on Twitter against recent Reddit comments, to compute text similarities based on the Word2Vec algorithm. Social media text is fetched against certain keywords related to COVID-19.

## Architecture

---


![Architecture](flowchart.svg?raw=true "Flow Diagram")


## Local Usage

---

Environment variables required -

```
TWITTER_BEARER_TOKEN
MONGODB_URI
AIRFLOW_HOME
```

Set up your virtual environment with Apache Airflow.
```
python3 -m venv airflowEnv
source airflowEnv/bin/activate
pip install -r airflow-scheduling/requirements.txt
```

Run these two commands
```
airflow webserver -p 8080
airflow scheduler
```

Visit `localhost:8080` and start the DAG, it will run periodically and you're good to go!

![UI](airflow-ui.png?raw=true "UI")


