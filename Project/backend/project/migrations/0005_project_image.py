# Generated by Django 2.1.4 on 2019-01-02 13:33

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('project', '0004_auto_20181217_1159'),
    ]

    operations = [
        migrations.AddField(
            model_name='project',
            name='image',
            field=models.ImageField(default='images/project/no-image.jpg', upload_to='images/project'),
        ),
    ]
