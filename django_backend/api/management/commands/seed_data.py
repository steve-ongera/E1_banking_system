"""
Custom Django management command to seed the database with sample banking data.
Run with: python manage.py seed_data
"""

from django.core.management.base import BaseCommand
from django.contrib.auth.hashers import make_password
from api.models import User
import random
from decimal import Decimal

class Command(BaseCommand):
    help = 'Seeds the database with sample banking users and data'

    def handle(self, *args, **options):
        self.stdout.write(self.style.SUCCESS('Starting database seeding...'))
        
        # Clear existing data (optional - comment out if you want to keep existing data)
        self.stdout.write('Clearing existing user data...')
        User.objects.all().delete()
        
        # Sample user data
        sample_users = [
            {
                'username': 'john_doe',
                'email': 'john@example.com',
                'password': 'password123',
                'first_name': 'John',
                'last_name': 'Doe',
                'phone': '+1234567890',
                'balance': Decimal('5000.00')
            },
            {
                'username': 'jane_smith',
                'email': 'jane@example.com',
                'password': 'password123',
                'first_name': 'Jane',
                'last_name': 'Smith',
                'phone': '+1234567891',
                'balance': Decimal('7500.50')
            },
            {
                'username': 'mike_johnson',
                'email': 'mike@example.com',
                'password': 'password123',
                'first_name': 'Mike',
                'last_name': 'Johnson',
                'phone': '+1234567892',
                'balance': Decimal('3250.75')
            },
            {
                'username': 'sarah_williams',
                'email': 'sarah@example.com',
                'password': 'password123',
                'first_name': 'Sarah',
                'last_name': 'Williams',
                'phone': '+1234567893',
                'balance': Decimal('10200.00')
            },
            {
                'username': 'robert_brown',
                'email': 'robert@example.com',
                'password': 'password123',
                'first_name': 'Robert',
                'last_name': 'Brown',
                'phone': '+1234567894',
                'balance': Decimal('2150.25')
            },
            {
                'username': 'emily_davis',
                'email': 'emily@example.com',
                'password': 'password123',
                'first_name': 'Emily',
                'last_name': 'Davis',
                'phone': '+1234567895',
                'balance': Decimal('8900.00')
            },
            {
                'username': 'david_miller',
                'email': 'david@example.com',
                'password': 'password123',
                'first_name': 'David',
                'last_name': 'Miller',
                'phone': '+1234567896',
                'balance': Decimal('4300.00')
            },
            {
                'username': 'lisa_wilson',
                'email': 'lisa@example.com',
                'password': 'password123',
                'first_name': 'Lisa',
                'last_name': 'Wilson',
                'phone': '+1234567897',
                'balance': Decimal('6700.50')
            },
            {
                'username': 'chris_moore',
                'email': 'chris@example.com',
                'password': 'password123',
                'first_name': 'Chris',
                'last_name': 'Moore',
                'phone': '+1234567898',
                'balance': Decimal('3100.00')
            },
            {
                'username': 'amanda_taylor',
                'email': 'amanda@example.com',
                'password': 'password123',
                'first_name': 'Amanda',
                'last_name': 'Taylor',
                'phone': '+1234567899',
                'balance': Decimal('15000.00')
            }
        ]
        
        # Create users
        users_created = 0
        for user_data in sample_users:
            # Create user with the custom User model
            user = User.objects.create(
                username=user_data['username'],
                email=user_data['email'],
                password=make_password(user_data['password']),  # Hash the password
                first_name=user_data['first_name'],
                last_name=user_data['last_name'],
                phone=user_data['phone'],
                balance=user_data['balance']
            )
            
            # Generate account number (format: ACCXXXXXXXX)
            user.account_number = f"ACC{user.id:08d}"
            user.save()
            
            users_created += 1
            self.stdout.write(f"  Created user: {user.username} (Account: {user.account_number}, Balance: ${user.balance})")
        
        # Create some additional random users with varying balances
        self.stdout.write('\nCreating additional random users...')
        
        first_names = ['Alex', 'Maria', 'James', 'Patricia', 'Michael', 'Jennifer', 'William', 'Linda']
        last_names = ['Anderson', 'Thomas', 'Jackson', 'White', 'Harris', 'Martin', 'Thompson', 'Garcia']
        
        for i in range(20):  # Create 20 random users
            first_name = random.choice(first_names)
            last_name = random.choice(last_names)
            username = f"{first_name.lower()}_{last_name.lower()}_{i+1}"
            
            # Random balance between $100 and $50000
            random_balance = Decimal(random.randint(100, 50000))
            
            user = User.objects.create(
                username=username,
                email=f"{username}@example.com",
                password=make_password('password123'),
                first_name=first_name,
                last_name=last_name,
                phone=f"+1{random.randint(1000000000, 9999999999)}",
                balance=random_balance
            )
            
            user.account_number = f"ACC{user.id:08d}"
            user.save()
            
            users_created += 1
            
            if (i + 1) % 5 == 0:  # Print every 5 users
                self.stdout.write(f"  Created {i+1} random users...")
        
        # Summary
        self.stdout.write(self.style.SUCCESS(f'\n✓ Database seeding completed successfully!'))
        self.stdout.write(self.style.SUCCESS(f'✓ Total users created: {users_created}'))
        
        # Calculate and display statistics
        total_balance = User.objects.aggregate_total_balance() if hasattr(User.objects, 'aggregate_total_balance') else sum(user.balance for user in User.objects.all())
        avg_balance = total_balance / users_created if users_created > 0 else 0
        
        self.stdout.write(self.style.SUCCESS(f'✓ Total balance across all accounts: ${total_balance:,.2f}'))
        self.stdout.write(self.style.SUCCESS(f'✓ Average account balance: ${avg_balance:,.2f}'))
        
        # Display sample login credentials
        self.stdout.write('\n' + '='*60)
        self.stdout.write(self.style.WARNING('SAMPLE LOGIN CREDENTIALS:'))
        self.stdout.write('='*60)
        for user in sample_users[:5]:  # Show first 5 sample users
            self.stdout.write(f"  Username: {user['username']} | Password: {user['password']}")
        self.stdout.write('\n  (All users have password: "password123")')
        self.stdout.write('='*60)